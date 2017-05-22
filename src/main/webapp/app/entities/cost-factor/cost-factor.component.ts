import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { CostFactor } from './cost-factor.model';
import { CostFactorService } from './cost-factor.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-cost-factor',
    templateUrl: './cost-factor.component.html'
})
export class CostFactorComponent implements OnInit, OnDestroy {
costFactors: CostFactor[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private costFactorService: CostFactorService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.costFactorService.query().subscribe(
            (res: Response) => {
                this.costFactors = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCostFactors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CostFactor) {
        return item.id;
    }
    registerChangeInCostFactors() {
        this.eventSubscriber = this.eventManager.subscribe('costFactorListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
