import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Code } from './code.model';
import { CodeService } from './code.service';

@Component({
    selector: 'jhi-code-detail',
    templateUrl: './code-detail.component.html'
})
export class CodeDetailComponent implements OnInit, OnDestroy {

    code: Code;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private codeService: CodeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCodes();
    }

    load(id) {
        this.codeService.find(id).subscribe((code) => {
            this.code = code;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCodes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'codeListModification',
            (response) => this.load(this.code.id)
        );
    }
}
