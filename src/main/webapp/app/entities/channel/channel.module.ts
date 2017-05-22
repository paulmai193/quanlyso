import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    ChannelService,
    ChannelPopupService,
    ChannelComponent,
    ChannelDetailComponent,
    ChannelDialogComponent,
    ChannelPopupComponent,
    ChannelDeletePopupComponent,
    ChannelDeleteDialogComponent,
    channelRoute,
    channelPopupRoute,
} from './';

const ENTITY_STATES = [
    ...channelRoute,
    ...channelPopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ChannelComponent,
        ChannelDetailComponent,
        ChannelDialogComponent,
        ChannelDeleteDialogComponent,
        ChannelPopupComponent,
        ChannelDeletePopupComponent,
    ],
    entryComponents: [
        ChannelComponent,
        ChannelDialogComponent,
        ChannelPopupComponent,
        ChannelDeleteDialogComponent,
        ChannelDeletePopupComponent,
    ],
    providers: [
        ChannelService,
        ChannelPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoChannelModule {}
