import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FactorDetailComponent } from '../../../../../../main/webapp/app/entities/factor/factor-detail.component';
import { FactorService } from '../../../../../../main/webapp/app/entities/factor/factor.service';
import { Factor } from '../../../../../../main/webapp/app/entities/factor/factor.model';

describe('Component Tests', () => {

    describe('Factor Management Detail Component', () => {
        let comp: FactorDetailComponent;
        let fixture: ComponentFixture<FactorDetailComponent>;
        let service: FactorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [FactorDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FactorService,
                    EventManager
                ]
            }).overrideComponent(FactorDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FactorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FactorService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Factor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.factor).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
