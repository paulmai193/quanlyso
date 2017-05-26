import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Factor } from './factor.model';
import { FactorPopupService } from './factor-popup.service';
import { FactorService } from './factor.service';

@Component({
    selector: 'jhi-factor-dialog',
    templateUrl: './factor-dialog.component.html'
})
export class FactorDialogComponent implements OnInit {

    factor: Factor;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private factorService: FactorService,
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
        this.isSaving = true;console.log(this.factor);
        if (this.factor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.factorService.update(this.factor));
        } else {
            this.subscribeToSaveResponse(
                this.factorService.create(this.factor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Factor>) {
        result.subscribe((res: Factor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Factor) {
        this.eventManager.broadcast({ name: 'factorListModification', content: 'OK'});
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
    selector: 'jhi-factor-popup',
    template: ''
})
export class FactorPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private factorPopupService: FactorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.factorPopupService
                    .open(FactorDialogComponent, params['id']);
            } else {
                this.modalRef = this.factorPopupService
                    .open(FactorDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
