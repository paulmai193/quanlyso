import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    StyleService,
    StylePopupService,
    StyleComponent,
    StyleDetailComponent,
    StyleDialogComponent,
    StylePopupComponent,
    StyleDeletePopupComponent,
    StyleDeleteDialogComponent,
    styleRoute,
    stylePopupRoute,
} from './';

const ENTITY_STATES = [
    ...styleRoute,
    ...stylePopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StyleComponent,
        StyleDetailComponent,
        StyleDialogComponent,
        StyleDeleteDialogComponent,
        StylePopupComponent,
        StyleDeletePopupComponent,
    ],
    entryComponents: [
        StyleComponent,
        StyleDialogComponent,
        StylePopupComponent,
        StyleDeleteDialogComponent,
        StyleDeletePopupComponent,
    ],
    providers: [
        StyleService,
        StylePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoStyleModule {}
