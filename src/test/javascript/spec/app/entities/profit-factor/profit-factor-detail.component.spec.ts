import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager, AlertService } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProfitFactorDetailComponent } from '../../../../../../main/webapp/app/entities/profit-factor/profit-factor-detail.component';
import { ProfitFactorService } from '../../../../../../main/webapp/app/entities/profit-factor/profit-factor.service';
import { ProfitFactor } from '../../../../../../main/webapp/app/entities/profit-factor/profit-factor.model';
import { FactorService } from '../../../../../../main/webapp/app/entities/factor/factor.service';
import { StyleService } from '../../../../../../main/webapp/app/entities/style/style.service';
import { TypesService } from '../../../../../../main/webapp/app/entities/types/types.service';

describe('Component Tests', () => {

    describe('ProfitFactor Management Detail Component', () => {
        let comp: ProfitFactorDetailComponent;
        let fixture: ComponentFixture<ProfitFactorDetailComponent>;
        let service: ProfitFactorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [ProfitFactorDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProfitFactorService,
                    AlertService,
                    FactorService,
                    StyleService,
                    TypesService,
                    EventManager
                ]
            }).overrideComponent(ProfitFactorDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfitFactorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfitFactorService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProfitFactor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.profitFactor).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
