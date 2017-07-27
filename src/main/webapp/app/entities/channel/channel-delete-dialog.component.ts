import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Channel } from './channel.model';
import { ChannelPopupService } from './channel-popup.service';
import { ChannelService } from './channel.service';

@Component({
    selector: 'jhi-channel-delete-dialog',
    templateUrl: './channel-delete-dialog.component.html'
})
export class ChannelDeleteDialogComponent {

    channel: Channel;

    constructor(
        private channelService: ChannelService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.channelService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'channelListModification',
                content: 'Deleted an channel'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-channel-delete-popup',
    template: ''
})
export class ChannelDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private channelPopupService: ChannelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.channelPopupService
                .open(ChannelDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
