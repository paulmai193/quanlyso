import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TransactionsDetailComponent } from '../../../../../../main/webapp/app/entities/transactions/transactions-detail.component';
import { TransactionsService } from '../../../../../../main/webapp/app/entities/transactions/transactions.service';
import { Transactions } from '../../../../../../main/webapp/app/entities/transactions/transactions.model';

describe('Component Tests', () => {

    describe('Transactions Management Detail Component', () => {
        let comp: TransactionsDetailComponent;
        let fixture: ComponentFixture<TransactionsDetailComponent>;
        let service: TransactionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [TransactionsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TransactionsService,
                    EventManager
                ]
            }).overrideComponent(TransactionsDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransactionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransactionsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Transactions(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.transactions).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
