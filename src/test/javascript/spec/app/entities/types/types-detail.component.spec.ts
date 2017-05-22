import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TypesDetailComponent } from '../../../../../../main/webapp/app/entities/types/types-detail.component';
import { TypesService } from '../../../../../../main/webapp/app/entities/types/types.service';
import { Types } from '../../../../../../main/webapp/app/entities/types/types.model';

describe('Component Tests', () => {

    describe('Types Management Detail Component', () => {
        let comp: TypesDetailComponent;
        let fixture: ComponentFixture<TypesDetailComponent>;
        let service: TypesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [TypesDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TypesService,
                    EventManager
                ]
            }).overrideComponent(TypesDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TypesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TypesService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Types(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.types).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
