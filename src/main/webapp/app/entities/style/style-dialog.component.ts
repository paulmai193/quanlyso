import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Style } from './style.model';
import { StylePopupService } from './style-popup.service';
import { StyleService } from './style.service';

@Component({
    selector: 'jhi-style-dialog',
    templateUrl: './style-dialog.component.html'
})
export class StyleDialogComponent implements OnInit {

    style: Style;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private styleService: StyleService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.style.id !== undefined) {
            this.subscribeToSaveResponse(
                this.styleService.update(this.style));
        } else {
            this.subscribeToSaveResponse(
                this.styleService.create(this.style));
        }
    }

    private subscribeToSaveResponse(result: Observable<Style>) {
        result.subscribe((res: Style) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Style) {
        this.eventManager.broadcast({ name: 'styleListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-style-popup',
    template: ''
})
export class StylePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stylePopupService: StylePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.stylePopupService
                    .open(StyleDialogComponent, params['id']);
            } else {
                this.modalRef = this.stylePopupService
                    .open(StyleDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
