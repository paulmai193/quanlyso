import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Channel } from './channel.model';
import { ChannelService } from './channel.service';

@Component({
    selector: 'jhi-channel-detail',
    templateUrl: './channel-detail.component.html'
})
export class ChannelDetailComponent implements OnInit, OnDestroy {

    channel: Channel;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private channelService: ChannelService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInChannels();
    }

    load(id) {
        this.channelService.find(id).subscribe((channel) => {
            this.channel = channel;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInChannels() {
        this.eventSubscriber = this.eventManager.subscribe(
            'channelListModification',
            (response) => this.load(this.channel.id)
        );
    }
}
