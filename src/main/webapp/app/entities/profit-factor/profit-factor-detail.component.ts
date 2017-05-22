import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { ProfitFactor } from './profit-factor.model';
import { ProfitFactorService } from './profit-factor.service';

@Component({
    selector: 'jhi-profit-factor-detail',
    templateUrl: './profit-factor-detail.component.html'
})
export class ProfitFactorDetailComponent implements OnInit, OnDestroy {

    profitFactor: ProfitFactor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private profitFactorService: ProfitFactorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProfitFactors();
    }

    load(id) {
        this.profitFactorService.find(id).subscribe((profitFactor) => {
            this.profitFactor = profitFactor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProfitFactors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'profitFactorListModification',
            (response) => this.load(this.profitFactor.id)
        );
    }
}
