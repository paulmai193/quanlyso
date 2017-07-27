import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit, Sanitizer } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager, AlertService } from 'ng-jhipster';
import { QuanlysoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CodeDetailComponent } from '../../../../../../main/webapp/app/entities/code/code-detail.component';
import { CodeService } from '../../../../../../main/webapp/app/entities/code/code.service';
import { Code } from '../../../../../../main/webapp/app/entities/code/code.model';
import { ChannelService } from '../../../../../../main/webapp/app/entities/channel/channel.service';

describe('Component Tests', () => {

    describe('Code Management Detail Component', () => {
        let comp: CodeDetailComponent;
        let fixture: ComponentFixture<CodeDetailComponent>;
        let service: CodeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QuanlysoTestModule],
                declarations: [CodeDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CodeService,
                    ChannelService,
                    EventManager
                ]
            }).overrideComponent(CodeDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CodeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CodeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Code(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.code).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
