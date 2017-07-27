import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProfitFactor } from './profit-factor.model';
import { ProfitFactorService } from './profit-factor.service';
@Injectable()
export class ProfitFactorPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private profitFactorService: ProfitFactorService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.profitFactorService.find(id).subscribe((profitFactor) => {
                this.profitFactorModalRef(component, profitFactor);
            });
        } else {
            return this.profitFactorModalRef(component, new ProfitFactor());
        }
    }

    profitFactorModalRef(component: Component, profitFactor: ProfitFactor): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.profitFactor = profitFactor;
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
