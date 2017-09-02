import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { ProfitFactor } from './profit-factor.model';
import { ProfitFactorService } from './profit-factor.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Style } from '../style/style.model';
import { StyleService } from '../style/style.service';

@Component({
    selector: 'jhi-profit-factor',
    templateUrl: './profit-factor.component.html'
})
export class ProfitFactorComponent implements OnInit, OnDestroy {
    profitFactors: ProfitFactor[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private principal: Principal,
        private profitFactorService: ProfitFactorService,
        private alertService: AlertService,
        private styleService: StyleService
    ) {
    }

    loadAll() {
        this.profitFactorService.query().subscribe(
            (res: Response) => {
                this.profitFactors = res.json();
                this.profitFactors.forEach((el: ProfitFactor) => {
                    this.getStyleName(el);
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

    private getStyleName(profitFactor: ProfitFactor): void {
        this.styleService.find(profitFactor.stylesId).toPromise()
            .then((style: Style) => profitFactor.stylesName = style.name)
            .catch((error: any) => {
                console.error('Cannot retrieve styles', error);
                this.onError(error);
            });
    }

}
