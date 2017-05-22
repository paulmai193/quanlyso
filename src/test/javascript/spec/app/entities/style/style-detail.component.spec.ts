import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StyleDetailComponent } from '../../../../../../main/webapp/app/entities/style/style-detail.component';
import { StyleService } from '../../../../../../main/webapp/app/entities/style/style.service';
import { Style } from '../../../../../../main/webapp/app/entities/style/style.model';

describe('Component Tests', () => {

    describe('Style Management Detail Component', () => {
        let comp: StyleDetailComponent;
        let fixture: ComponentFixture<StyleDetailComponent>;
        let service: StyleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [StyleDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StyleService,
                    EventManager
                ]
            }).overrideComponent(StyleDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StyleDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StyleService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Style(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.style).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
