import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ChannelDetailComponent } from '../../../../../../main/webapp/app/entities/channel/channel-detail.component';
import { ChannelService } from '../../../../../../main/webapp/app/entities/channel/channel.service';
import { Channel } from '../../../../../../main/webapp/app/entities/channel/channel.model';

describe('Component Tests', () => {

    describe('Channel Management Detail Component', () => {
        let comp: ChannelDetailComponent;
        let fixture: ComponentFixture<ChannelDetailComponent>;
        let service: ChannelService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [ChannelDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ChannelService,
                    EventManager
                ]
            }).overrideComponent(ChannelDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChannelDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChannelService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Channel(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.channel).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
