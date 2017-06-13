import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { ProfitFactor } from './profit-factor.model';
import { ProfitFactorService } from './profit-factor.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Factor } from '../factor/factor.model';
import { Style } from '../style/style.model';
import { Types } from '../types/types.model';
import { FactorService } from '../factor/factor.service';
import { StyleService } from '../style/style.service';
import { TypesService } from '../types/types.service';

@Component({
    selector: 'jhi-profit-factor',
    templateUrl: './profit-factor.component.html'
})
export class ProfitFactorComponent implements OnInit, OnDestroy {
profitFactors: ProfitFactor[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private profitFactorService: ProfitFactorService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal,
        private factorService: FactorService,
        private styleService: StyleService,
        private typeService: TypesService
    ) {
    }

    loadAll() {
        this.profitFactorService.query().subscribe(
            (res: Response) => {
                this.profitFactors = res.json();
                this.profitFactors.forEach((el: ProfitFactor) => {
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
        this.registerChangeInProfitFactors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProfitFactor) {
        return item.id;
    }
    registerChangeInProfitFactors() {
        this.eventSubscriber = this.eventManager.subscribe('profitFactorListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    private getFactorName(profitFactor: ProfitFactor): void {
        this.factorService.find(profitFactor.factorsId).subscribe((factor: Factor) => {
            profitFactor.factorsName = factor.name;
        });
    }

    private getStyleName(profitFactor: ProfitFactor): void {
        this.styleService.find(profitFactor.stylesId).subscribe((style: Style) => {
            profitFactor.stylesName = style.name;
        });
    }

    private getTypeName(profitFactor: ProfitFactor): void {
        this.typeService.find(profitFactor.typesId).subscribe((type: Types) => {
            profitFactor.typesName = type.name;
        });
    }

}
