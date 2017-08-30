import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { CostFactor } from './cost-factor.model';
import { CostFactorPopupService } from './cost-factor-popup.service';
import { CostFactorService } from './cost-factor.service';
import { Style, StyleService } from '../style';
import { Types, TypesService } from '../types';

@Component({
    selector: 'jhi-cost-factor-dialog',
    templateUrl: './cost-factor-dialog.component.html'
})
export class CostFactorDialogComponent implements OnInit {

    costFactor: CostFactor;
    authorities: any[];
    isSaving: boolean;

    styles: Style[];

    types: Types[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private costFactorService: CostFactorService,
        private styleService: StyleService,
        private typesService: TypesService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.styleService.query().subscribe(
            (res: Response) => { this.styles = res.json(); }, (res: Response) => this.onError(res.json()));
        this.typesService.query().subscribe(
            (res: Response) => { this.types = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.costFactor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.costFactorService.update(this.costFactor));
        } else {
            this.subscribeToSaveResponse(
                this.costFactorService.create(this.costFactor));
        }
    }

    private subscribeToSaveResponse(result: Observable<CostFactor>) {
        result.subscribe((res: CostFactor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CostFactor) {
        this.eventManager.broadcast({ name: 'costFactorListModification', content: 'OK'});
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

    trackStyleById(index: number, item: Style) {
        return item.id;
    }

    trackTypesById(index: number, item: Types) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cost-factor-popup',
    template: ''
})
export class CostFactorPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private costFactorPopupService: CostFactorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.costFactorPopupService
                    .open(CostFactorDialogComponent, params['id']);
            } else {
                this.modalRef = this.costFactorPopupService
                    .open(CostFactorDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
