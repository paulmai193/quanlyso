import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Types } from './types.model';
import { TypesService } from './types.service';

@Component({
    selector: 'jhi-types-detail',
    templateUrl: './types-detail.component.html'
})
export class TypesDetailComponent implements OnInit, OnDestroy {

    types: Types;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private typesService: TypesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTypes();
    }

    load(id) {
        this.typesService.find(id).subscribe((types) => {
            this.types = types;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'typesListModification',
            (response) => this.load(this.types.id)
        );
    }
}
