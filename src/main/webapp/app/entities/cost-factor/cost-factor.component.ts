import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { CostFactor } from './cost-factor.model';
import { CostFactorService } from './cost-factor.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { FactorService } from '../factor/factor.service';
import { Factor } from '../factor/factor.model';
import { StyleService } from '../style/style.service';
import { Style } from '../style/style.model';
import { TypesService } from '../types/types.service';
import { Types } from '../types/types.model';

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
        private principal: Principal,
        private factorService: FactorService,
        private styleService: StyleService,
        private typeService: TypesService
    ) {
    }

    loadAll() {
        this.costFactorService.query().subscribe(
            (res: Response) => {
                this.costFactors = res.json();
                this.costFactors.forEach((el: CostFactor) => {
                    this.getFactorName(el);
                    this.getStyleName(el);
                    this.getTypeName(el);
                });
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

    private getFactorName(costFactor: CostFactor): void {
        this.factorService.find(costFactor.factorsId).subscribe((factor: Factor) => {
            costFactor.factorsName = factor.name;
        });
    }

    private getStyleName(costFactor: CostFactor): void {
        this.styleService.find(costFactor.stylesId).subscribe((style: Style) => {
            costFactor.stylesName = style.name;
        });
    }

    private getTypeName(costFactor: CostFactor): void {
        this.typeService.find(costFactor.typesId).subscribe((type: Types) => {
            costFactor.typesName = type.name;
        });
    }
}
