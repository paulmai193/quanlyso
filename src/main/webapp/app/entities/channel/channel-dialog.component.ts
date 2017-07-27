import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Channel } from './channel.model';
import { ChannelPopupService } from './channel-popup.service';
import { ChannelService } from './channel.service';

@Component({
    selector: 'jhi-channel-dialog',
    templateUrl: './channel-dialog.component.html'
})
export class ChannelDialogComponent implements OnInit {

    channel: Channel;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private channelService: ChannelService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.channel.id !== undefined) {
            this.subscribeToSaveResponse(
                this.channelService.update(this.channel));
        } else {
            this.subscribeToSaveResponse(
                this.channelService.create(this.channel));
        }
    }

    private subscribeToSaveResponse(result: Observable<Channel>) {
        result.subscribe((res: Channel) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Channel) {
        this.eventManager.broadcast({ name: 'channelListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-channel-popup',
    template: ''
})
export class ChannelPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private channelPopupService: ChannelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.channelPopupService
                    .open(ChannelDialogComponent, params['id']);
            } else {
                this.modalRef = this.channelPopupService
                    .open(ChannelDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
