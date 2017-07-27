import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    TypesService,
    TypesPopupService,
    TypesComponent,
    TypesDetailComponent,
    TypesDialogComponent,
    TypesPopupComponent,
    TypesDeletePopupComponent,
    TypesDeleteDialogComponent,
    typesRoute,
    typesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...typesRoute,
    ...typesPopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TypesComponent,
        TypesDetailComponent,
        TypesDialogComponent,
        TypesDeleteDialogComponent,
        TypesPopupComponent,
        TypesDeletePopupComponent,
    ],
    entryComponents: [
        TypesComponent,
        TypesDialogComponent,
        TypesPopupComponent,
        TypesDeleteDialogComponent,
        TypesDeletePopupComponent,
    ],
    providers: [
        TypesService,
        TypesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoTypesModule {}
