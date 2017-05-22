import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    FactorService,
    FactorPopupService,
    FactorComponent,
    FactorDetailComponent,
    FactorDialogComponent,
    FactorPopupComponent,
    FactorDeletePopupComponent,
    FactorDeleteDialogComponent,
    factorRoute,
    factorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...factorRoute,
    ...factorPopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        FactorComponent,
        FactorDetailComponent,
        FactorDialogComponent,
        FactorDeleteDialogComponent,
        FactorPopupComponent,
        FactorDeletePopupComponent,
    ],
    entryComponents: [
        FactorComponent,
        FactorDialogComponent,
        FactorPopupComponent,
        FactorDeleteDialogComponent,
        FactorDeletePopupComponent,
    ],
    providers: [
        FactorService,
        FactorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoFactorModule {}
