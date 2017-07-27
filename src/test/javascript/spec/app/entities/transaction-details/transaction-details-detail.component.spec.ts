import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TransactionDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/transaction-details/transaction-details-detail.component';
import { TransactionDetailsService } from '../../../../../../main/webapp/app/entities/transaction-details/transaction-details.service';
import { TransactionDetails } from '../../../../../../main/webapp/app/entities/transaction-details/transaction-details.model';

describe('Component Tests', () => {

    describe('TransactionDetails Management Detail Component', () => {
        let comp: TransactionDetailsDetailComponent;
        let fixture: ComponentFixture<TransactionDetailsDetailComponent>;
        let service: TransactionDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [TransactionDetailsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TransactionDetailsService,
                    EventManager
                ]
            }).overrideComponent(TransactionDetailsDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransactionDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransactionDetailsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TransactionDetails(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.transactionDetails).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
