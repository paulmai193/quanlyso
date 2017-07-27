import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Style } from './style.model';
import { StylePopupService } from './style-popup.service';
import { StyleService } from './style.service';

@Component({
    selector: 'jhi-style-delete-dialog',
    templateUrl: './style-delete-dialog.component.html'
})
export class StyleDeleteDialogComponent {

    style: Style;

    constructor(
        private styleService: StyleService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.styleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'styleListModification',
                content: 'Deleted an style'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-style-delete-popup',
    template: ''
})
export class StyleDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stylePopupService: StylePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.stylePopupService
                .open(StyleDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
