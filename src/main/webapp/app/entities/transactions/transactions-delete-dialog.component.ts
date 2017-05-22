import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Transactions } from './transactions.model';
import { TransactionsPopupService } from './transactions-popup.service';
import { TransactionsService } from './transactions.service';

@Component({
    selector: 'jhi-transactions-delete-dialog',
    templateUrl: './transactions-delete-dialog.component.html'
})
export class TransactionsDeleteDialogComponent {

    transactions: Transactions;

    constructor(
        private transactionsService: TransactionsService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.transactionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'transactionsListModification',
                content: 'Deleted an transactions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-transactions-delete-popup',
    template: ''
})
export class TransactionsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transactionsPopupService: TransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.transactionsPopupService
                .open(TransactionsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
