import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ClientDetailComponent } from '../../../../../../main/webapp/app/entities/client/client-detail.component';
import { ClientService } from '../../../../../../main/webapp/app/entities/client/client.service';
import { Client } from '../../../../../../main/webapp/app/entities/client/client.model';

describe('Component Tests', () => {

    describe('Client Management Detail Component', () => {
        let comp: ClientDetailComponent;
        let fixture: ComponentFixture<ClientDetailComponent>;
        let service: ClientService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [ClientDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ClientService,
                    EventManager
                ]
            }).overrideComponent(ClientDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClientDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClientService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Client(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.client).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
