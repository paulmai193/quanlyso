import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { TransactionDetails } from './transaction-details.model';
import { TransactionDetailsPopupService } from './transaction-details-popup.service';
import { TransactionDetailsService } from './transaction-details.service';
import { Transactions, TransactionsService } from '../transactions';
import { Channel, ChannelService } from '../channel';
import { Factor, FactorService } from '../factor';
import { Style, StyleService } from '../style';
import { Types, TypesService } from '../types';

@Component({
    selector: 'jhi-transaction-details-dialog',
    templateUrl: './transaction-details-dialog.component.html'
})
export class TransactionDetailsDialogComponent implements OnInit {

    transactionDetails: TransactionDetails;
    authorities: any[];
    isSaving: boolean;

    transactions: Transactions[];

    channels: Channel[];

    factors: Factor[];

    styles: Style[];

    types: Types[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private transactionDetailsService: TransactionDetailsService,
        private transactionsService: TransactionsService,
        private channelService: ChannelService,
        private factorService: FactorService,
        private styleService: StyleService,
        private typesService: TypesService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.transactionsService.query().subscribe(
            (res: Response) => { this.transactions = res.json(); }, (res: Response) => this.onError(res.json()));
        this.channelService.query().subscribe(
            (res: Response) => { this.channels = res.json(); }, (res: Response) => this.onError(res.json()));
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
        if (this.transactionDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transactionDetailsService.update(this.transactionDetails));
        } else {
            this.subscribeToSaveResponse(
                this.transactionDetailsService.create(this.transactionDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<TransactionDetails>) {
        result.subscribe((res: TransactionDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TransactionDetails) {
        this.eventManager.broadcast({ name: 'transactionDetailsListModification', content: 'OK'});
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

    trackTransactionsById(index: number, item: Transactions) {
        return item.id;
    }

    trackChannelById(index: number, item: Channel) {
        return item.id;
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
    selector: 'jhi-transaction-details-popup',
    template: ''
})
export class TransactionDetailsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transactionDetailsPopupService: TransactionDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.transactionDetailsPopupService
                    .open(TransactionDetailsDialogComponent, params['id']);
            } else {
                this.modalRef = this.transactionDetailsPopupService
                    .open(TransactionDetailsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
