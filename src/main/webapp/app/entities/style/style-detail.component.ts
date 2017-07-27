import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Style } from './style.model';
import { StyleService } from './style.service';

@Component({
    selector: 'jhi-style-detail',
    templateUrl: './style-detail.component.html'
})
export class StyleDetailComponent implements OnInit, OnDestroy {

    style: Style;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private styleService: StyleService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStyles();
    }

    load(id) {
        this.styleService.find(id).subscribe((style) => {
            this.style = style;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStyles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'styleListModification',
            (response) => this.load(this.style.id)
        );
    }
}
