import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'filemasterproApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'project',
    data: { pageTitle: 'filemasterproApp.project.home.title' },
    loadChildren: () => import('./project/project.routes'),
  },
  {
    path: 'file',
    data: { pageTitle: 'filemasterproApp.file.home.title' },
    loadChildren: () => import('./file/file.routes'),
  },
  {
    path: 'vendor',
    data: { pageTitle: 'filemasterproApp.vendor.home.title' },
    loadChildren: () => import('./vendor/vendor.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
