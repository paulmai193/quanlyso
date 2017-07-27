import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Types } from './types.model';
import { TypesPopupService } from './types-popup.service';
import { TypesService } from './types.service';

@Component({
    selector: 'jhi-types-delete-dialog',
    templateUrl: './types-delete-dialog.component.html'
})
export class TypesDeleteDialogComponent {

    types: Types;

    constructor(
        private typesService: TypesService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.typesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'typesListModification',
                content: 'Deleted an types'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-types-delete-popup',
    template: ''
})
export class TypesDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private typesPopupService: TypesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.typesPopupService
                .open(TypesDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
