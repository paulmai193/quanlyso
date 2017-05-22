import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { TransactionDetails } from './transaction-details.model';
import { TransactionDetailsService } from './transaction-details.service';

@Component({
    selector: 'jhi-transaction-details-detail',
    templateUrl: './transaction-details-detail.component.html'
})
export class TransactionDetailsDetailComponent implements OnInit, OnDestroy {

    transactionDetails: TransactionDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private transactionDetailsService: TransactionDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTransactionDetails();
    }

    load(id) {
        this.transactionDetailsService.find(id).subscribe((transactionDetails) => {
            this.transactionDetails = transactionDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransactionDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'transactionDetailsListModification',
            (response) => this.load(this.transactionDetails.id)
        );
    }
}
