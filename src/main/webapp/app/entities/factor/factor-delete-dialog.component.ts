import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Factor } from './factor.model';
import { FactorPopupService } from './factor-popup.service';
import { FactorService } from './factor.service';

@Component({
    selector: 'jhi-factor-delete-dialog',
    templateUrl: './factor-delete-dialog.component.html'
})
export class FactorDeleteDialogComponent {

    factor: Factor;

    constructor(
        private factorService: FactorService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.factorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'factorListModification',
                content: 'Deleted an factor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-factor-delete-popup',
    template: ''
})
export class FactorDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private factorPopupService: FactorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.factorPopupService
                .open(FactorDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
