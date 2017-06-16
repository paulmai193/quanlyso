import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Transactions } from './transactions.model';
import { TransactionsPopupService } from './transactions-popup.service';
import { TransactionsService } from './transactions.service';
import { Client, ClientService } from '../client';

@Component({
    selector: 'jhi-transactions-dialog',
    templateUrl: './transactions-dialog.component.html'
})
export class TransactionsDialogComponent implements OnInit {

    transactions: Transactions;
    authorities: any[];
    isSaving: boolean;

    clients: Client[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private transactionsService: TransactionsService,
        private clientService: ClientService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.clientService.findByUserRole().subscribe(
            (res: Response) => { this.clients = res.json(); }, (res: Response) => this.onError(res.json()));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.transactions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transactionsService.update(this.transactions));
        } else {
            this.subscribeToSaveResponse(
                this.transactionsService.create(this.transactions));
        }
    }

    private subscribeToSaveResponse(result: Observable<Transactions>) {
        result.subscribe((res: Transactions) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Transactions) {
        this.eventManager.broadcast({ name: 'transactionsListModification', content: 'OK'});
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

    trackClientById(index: number, item: Client) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-transactions-popup',
    template: ''
})
export class TransactionsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transactionsPopupService: TransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.transactionsPopupService
                    .open(TransactionsDialogComponent, params['id']);
            } else {
                this.modalRef = this.transactionsPopupService
                    .open(TransactionsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
