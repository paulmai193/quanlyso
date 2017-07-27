import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    CostFactorService,
    CostFactorPopupService,
    CostFactorComponent,
    CostFactorDetailComponent,
    CostFactorDialogComponent,
    CostFactorPopupComponent,
    CostFactorDeletePopupComponent,
    CostFactorDeleteDialogComponent,
    costFactorRoute,
    costFactorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...costFactorRoute,
    ...costFactorPopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CostFactorComponent,
        CostFactorDetailComponent,
        CostFactorDialogComponent,
        CostFactorDeleteDialogComponent,
        CostFactorPopupComponent,
        CostFactorDeletePopupComponent,
    ],
    entryComponents: [
        CostFactorComponent,
        CostFactorDialogComponent,
        CostFactorPopupComponent,
        CostFactorDeleteDialogComponent,
        CostFactorDeletePopupComponent,
    ],
    providers: [
        CostFactorService,
        CostFactorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoCostFactorModule {}
