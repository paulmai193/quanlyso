import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { CostFactor } from './cost-factor.model';
import { CostFactorPopupService } from './cost-factor-popup.service';
import { CostFactorService } from './cost-factor.service';

@Component({
    selector: 'jhi-cost-factor-delete-dialog',
    templateUrl: './cost-factor-delete-dialog.component.html'
})
export class CostFactorDeleteDialogComponent {

    costFactor: CostFactor;

    constructor(
        private costFactorService: CostFactorService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.costFactorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'costFactorListModification',
                content: 'Deleted an costFactor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cost-factor-delete-popup',
    template: ''
})
export class CostFactorDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private costFactorPopupService: CostFactorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.costFactorPopupService
                .open(CostFactorDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
