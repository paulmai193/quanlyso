import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Code } from './code.model';
import { CodePopupService } from './code-popup.service';
import { CodeService } from './code.service';
import { Channel, ChannelService } from '../channel';

@Component({
    selector: 'jhi-code-dialog',
    templateUrl: './code-dialog.component.html'
})
export class CodeDialogComponent implements OnInit {

    code: Code;
    authorities: any[];
    isSaving: boolean;

    channels: Channel[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private codeService: CodeService,
        private channelService: ChannelService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.channelService.query().subscribe(
            (res: Response) => { this.channels = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.code.id !== undefined) {
            this.subscribeToSaveResponse(
                this.codeService.update(this.code));
        } else {
            this.subscribeToSaveResponse(
                this.codeService.create(this.code));
        }
    }

    private subscribeToSaveResponse(result: Observable<Code>) {
        result.subscribe((res: Code) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Code) {
        this.eventManager.broadcast({ name: 'codeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackChannelById(index: number, item: Channel) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-code-popup',
    template: ''
})
export class CodePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private codePopupService: CodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.codePopupService
                    .open(CodeDialogComponent, params['id']);
            } else {
                this.modalRef = this.codePopupService
                    .open(CodeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
