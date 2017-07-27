import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { ProfitFactor } from './profit-factor.model';
import { ProfitFactorPopupService } from './profit-factor-popup.service';
import { ProfitFactorService } from './profit-factor.service';

@Component({
    selector: 'jhi-profit-factor-delete-dialog',
    templateUrl: './profit-factor-delete-dialog.component.html'
})
export class ProfitFactorDeleteDialogComponent {

    profitFactor: ProfitFactor;

    constructor(
        private profitFactorService: ProfitFactorService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.profitFactorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'profitFactorListModification',
                content: 'Deleted an profitFactor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-profit-factor-delete-popup',
    template: ''
})
export class ProfitFactorDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private profitFactorPopupService: ProfitFactorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.profitFactorPopupService
                .open(ProfitFactorDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
