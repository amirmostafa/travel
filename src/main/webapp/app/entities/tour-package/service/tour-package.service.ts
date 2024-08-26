import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITourPackage, NewTourPackage } from '../tour-package.model';

export type PartialUpdateTourPackage = Partial<ITourPackage> & Pick<ITourPackage, 'id'>;

export type EntityResponseType = HttpResponse<ITourPackage>;
export type EntityArrayResponseType = HttpResponse<ITourPackage[]>;

@Injectable({ providedIn: 'root' })
export class TourPackageService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tour-packages');

  create(tourPackage: NewTourPackage): Observable<EntityResponseType> {
    return this.http.post<ITourPackage>(this.resourceUrl, tourPackage, { observe: 'response' });
  }

  update(tourPackage: ITourPackage): Observable<EntityResponseType> {
    return this.http.put<ITourPackage>(`${this.resourceUrl}/${this.getTourPackageIdentifier(tourPackage)}`, tourPackage, {
      observe: 'response',
    });
  }

  partialUpdate(tourPackage: PartialUpdateTourPackage): Observable<EntityResponseType> {
    return this.http.patch<ITourPackage>(`${this.resourceUrl}/${this.getTourPackageIdentifier(tourPackage)}`, tourPackage, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITourPackage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITourPackage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTourPackageIdentifier(tourPackage: Pick<ITourPackage, 'id'>): number {
    return tourPackage.id;
  }

  compareTourPackage(o1: Pick<ITourPackage, 'id'> | null, o2: Pick<ITourPackage, 'id'> | null): boolean {
    return o1 && o2 ? this.getTourPackageIdentifier(o1) === this.getTourPackageIdentifier(o2) : o1 === o2;
  }

  addTourPackageToCollectionIfMissing<Type extends Pick<ITourPackage, 'id'>>(
    tourPackageCollection: Type[],
    ...tourPackagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tourPackages: Type[] = tourPackagesToCheck.filter(isPresent);
    if (tourPackages.length > 0) {
      const tourPackageCollectionIdentifiers = tourPackageCollection.map(tourPackageItem => this.getTourPackageIdentifier(tourPackageItem));
      const tourPackagesToAdd = tourPackages.filter(tourPackageItem => {
        const tourPackageIdentifier = this.getTourPackageIdentifier(tourPackageItem);
        if (tourPackageCollectionIdentifiers.includes(tourPackageIdentifier)) {
          return false;
        }
        tourPackageCollectionIdentifiers.push(tourPackageIdentifier);
        return true;
      });
      return [...tourPackagesToAdd, ...tourPackageCollection];
    }
    return tourPackageCollection;
  }
}
