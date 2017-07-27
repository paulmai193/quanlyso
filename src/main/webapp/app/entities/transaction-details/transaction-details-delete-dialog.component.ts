import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { TransactionDetails } from './transaction-details.model';
import { TransactionDetailsPopupService } from './transaction-details-popup.service';
import { TransactionDetailsService } from './transaction-details.service';

@Component({
    selector: 'jhi-transaction-details-delete-dialog',
    templateUrl: './transaction-details-delete-dialog.component.html'
})
export class TransactionDetailsDeleteDialogComponent {

    transactionDetails: TransactionDetails;

    constructor(
        private transactionDetailsService: TransactionDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.transactionDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'transactionDetailsListModification',
                content: 'Deleted an transactionDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-transaction-details-delete-popup',
    template: ''
})
export class TransactionDetailsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transactionDetailsPopupService: TransactionDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.transactionDetailsPopupService
                .open(TransactionDetailsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
