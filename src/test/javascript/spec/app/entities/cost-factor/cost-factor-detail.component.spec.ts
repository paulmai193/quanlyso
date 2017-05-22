import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CostFactorDetailComponent } from '../../../../../../main/webapp/app/entities/cost-factor/cost-factor-detail.component';
import { CostFactorService } from '../../../../../../main/webapp/app/entities/cost-factor/cost-factor.service';
import { CostFactor } from '../../../../../../main/webapp/app/entities/cost-factor/cost-factor.model';

describe('Component Tests', () => {

    describe('CostFactor Management Detail Component', () => {
        let comp: CostFactorDetailComponent;
        let fixture: ComponentFixture<CostFactorDetailComponent>;
        let service: CostFactorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [CostFactorDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CostFactorService,
                    EventManager
                ]
            }).overrideComponent(CostFactorDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CostFactorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CostFactorService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CostFactor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.costFactor).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
