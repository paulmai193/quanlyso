import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CostFactor } from './cost-factor.model';
import { CostFactorService } from './cost-factor.service';
@Injectable()
export class CostFactorPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private costFactorService: CostFactorService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.costFactorService.find(id).subscribe((costFactor) => {
                this.costFactorModalRef(component, costFactor);
            });
        } else {
            return this.costFactorModalRef(component, new CostFactor());
        }
    }

    costFactorModalRef(component: Component, costFactor: CostFactor): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.costFactor = costFactor;
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
