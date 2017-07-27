import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Factor } from './factor.model';
import { FactorService } from './factor.service';

@Component({
    selector: 'jhi-factor-detail',
    templateUrl: './factor-detail.component.html'
})
export class FactorDetailComponent implements OnInit, OnDestroy {

    factor: Factor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private factorService: FactorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFactors();
    }

    load(id) {
        this.factorService.find(id).subscribe((factor) => {
            this.factor = factor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFactors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'factorListModification',
            (response) => this.load(this.factor.id)
        );
    }
}
