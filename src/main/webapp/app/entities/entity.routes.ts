import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'travelApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'agency',
    data: { pageTitle: 'travelApp.agency.home.title' },
    loadChildren: () => import('./agency/agency.routes'),
  },
  {
    path: 'tour-package',
    data: { pageTitle: 'travelApp.tourPackage.home.title' },
    loadChildren: () => import('./tour-package/tour-package.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'travelApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'booking',
    data: { pageTitle: 'travelApp.booking.home.title' },
    loadChildren: () => import('./booking/booking.routes'),
  },
  {
    path: 'hotel',
    data: { pageTitle: 'travelApp.hotel.home.title' },
    loadChildren: () => import('./hotel/hotel.routes'),
  },
  {
    path: 'room',
    data: { pageTitle: 'travelApp.room.home.title' },
    loadChildren: () => import('./room/room.routes'),
  },
  {
    path: 'room-price',
    data: { pageTitle: 'travelApp.roomPrice.home.title' },
    loadChildren: () => import('./room-price/room-price.routes'),
  },
  {
    path: 'currency',
    data: { pageTitle: 'travelApp.currency.home.title' },
    loadChildren: () => import('./currency/currency.routes'),
  },
  {
    path: 'testimonial',
    data: { pageTitle: 'travelApp.testimonial.home.title' },
    loadChildren: () => import('./testimonial/testimonial.routes'),
  },
  {
    path: 'about-us',
    data: { pageTitle: 'travelApp.aboutUs.home.title' },
    loadChildren: () => import('./about-us/about-us.routes'),
  },
  {
    path: 'agency-service',
    data: { pageTitle: 'travelApp.agencyService.home.title' },
    loadChildren: () => import('./agency-service/agency-service.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'travelApp.payment.home.title' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'loyalty-program',
    data: { pageTitle: 'travelApp.loyaltyProgram.home.title' },
    loadChildren: () => import('./loyalty-program/loyalty-program.routes'),
  },
  {
    path: 'loyalty-transaction',
    data: { pageTitle: 'travelApp.loyaltyTransaction.home.title' },
    loadChildren: () => import('./loyalty-transaction/loyalty-transaction.routes'),
  },
  {
    path: 'image',
    data: { pageTitle: 'travelApp.image.home.title' },
    loadChildren: () => import('./image/image.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
