import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Types } from './types.model';
import { TypesPopupService } from './types-popup.service';
import { TypesService } from './types.service';

@Component({
    selector: 'jhi-types-dialog',
    templateUrl: './types-dialog.component.html'
})
export class TypesDialogComponent implements OnInit {

    types: Types;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private typesService: TypesService,
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
        if (this.types.id !== undefined) {
            this.subscribeToSaveResponse(
                this.typesService.update(this.types));
        } else {
            this.subscribeToSaveResponse(
                this.typesService.create(this.types));
        }
    }

    private subscribeToSaveResponse(result: Observable<Types>) {
        result.subscribe((res: Types) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Types) {
        this.eventManager.broadcast({ name: 'typesListModification', content: 'OK'});
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
    selector: 'jhi-types-popup',
    template: ''
})
export class TypesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private typesPopupService: TypesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.typesPopupService
                    .open(TypesDialogComponent, params['id']);
            } else {
                this.modalRef = this.typesPopupService
                    .open(TypesDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
