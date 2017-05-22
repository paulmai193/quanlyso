import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { ProfitFactor } from './profit-factor.model';
import { ProfitFactorPopupService } from './profit-factor-popup.service';
import { ProfitFactorService } from './profit-factor.service';
import { Factor, FactorService } from '../factor';
import { Style, StyleService } from '../style';
import { Types, TypesService } from '../types';

@Component({
    selector: 'jhi-profit-factor-dialog',
    templateUrl: './profit-factor-dialog.component.html'
})
export class ProfitFactorDialogComponent implements OnInit {

    profitFactor: ProfitFactor;
    authorities: any[];
    isSaving: boolean;

    factors: Factor[];

    styles: Style[];

    types: Types[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private profitFactorService: ProfitFactorService,
        private factorService: FactorService,
        private styleService: StyleService,
        private typesService: TypesService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.factorService.query().subscribe(
            (res: Response) => { this.factors = res.json(); }, (res: Response) => this.onError(res.json()));
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
        if (this.profitFactor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.profitFactorService.update(this.profitFactor));
        } else {
            this.subscribeToSaveResponse(
                this.profitFactorService.create(this.profitFactor));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProfitFactor>) {
        result.subscribe((res: ProfitFactor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ProfitFactor) {
        this.eventManager.broadcast({ name: 'profitFactorListModification', content: 'OK'});
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

    trackFactorById(index: number, item: Factor) {
        return item.id;
    }

    trackStyleById(index: number, item: Style) {
        return item.id;
    }

    trackTypesById(index: number, item: Types) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-profit-factor-popup',
    template: ''
})
export class ProfitFactorPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private profitFactorPopupService: ProfitFactorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.profitFactorPopupService
                    .open(ProfitFactorDialogComponent, params['id']);
            } else {
                this.modalRef = this.profitFactorPopupService
                    .open(ProfitFactorDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
