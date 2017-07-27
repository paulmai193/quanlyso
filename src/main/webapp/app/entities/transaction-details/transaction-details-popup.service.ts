import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TransactionDetails } from './transaction-details.model';
import { TransactionDetailsService } from './transaction-details.service';
@Injectable()
export class TransactionDetailsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private transactionDetailsService: TransactionDetailsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.transactionDetailsService.find(id).subscribe((transactionDetails) => {
                this.transactionDetailsModalRef(component, transactionDetails);
            });
        } else {
            return this.transactionDetailsModalRef(component, new TransactionDetails());
        }
    }

    transactionDetailsModalRef(component: Component, transactionDetails: TransactionDetails): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.transactionDetails = transactionDetails;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
