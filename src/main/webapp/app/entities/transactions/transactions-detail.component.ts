import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Transactions } from './transactions.model';
import { TransactionsService } from './transactions.service';

@Component({
    selector: 'jhi-transactions-detail',
    templateUrl: './transactions-detail.component.html'
})
export class TransactionsDetailComponent implements OnInit, OnDestroy {

    transactions: Transactions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private transactionsService: TransactionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTransactions();
    }

    load(id) {
        this.transactionsService.find(id).subscribe((transactions) => {
            this.transactions = transactions;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransactions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'transactionsListModification',
            (response) => this.load(this.transactions.id)
        );
    }
}
