import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { AlertService, EventManager } from 'ng-jhipster';

import { ProfitFactor } from './profit-factor.model';
import { ProfitFactorService } from './profit-factor.service';
import { StyleService } from '../style/style.service';
import { TypesService } from '../types/types.service';
import { Style } from '../style/style.model';
import { Types } from '../types/types.model';

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
        private alertService: AlertService,
        private styleService: StyleService,
        private typeService: TypesService,
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
            this.getStyleName(this.profitFactor);
            this.getTypeName(this.profitFactor);
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

    private getTypeName(profitFactor: ProfitFactor): void {
        this.typeService.find(profitFactor.typesId).toPromise()
            .then((type: Types) => profitFactor.typesName = type.name)
            .catch((error: any) => {
                console.error('Cannot retrieve types', error);
                this.onError(error);
            });
    }

}
