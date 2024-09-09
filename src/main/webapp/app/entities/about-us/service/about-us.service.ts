import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAboutUs, NewAboutUs } from '../about-us.model';

export type PartialUpdateAboutUs = Partial<IAboutUs> & Pick<IAboutUs, 'id'>;

export type EntityResponseType = HttpResponse<IAboutUs>;
export type EntityArrayResponseType = HttpResponse<IAboutUs[]>;

@Injectable({ providedIn: 'root' })
export class AboutUsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aboutuses');

  create(aboutUs: NewAboutUs): Observable<EntityResponseType> {
    return this.http.post<IAboutUs>(this.resourceUrl, aboutUs, { observe: 'response' });
  }

  update(aboutUs: IAboutUs): Observable<EntityResponseType> {
    return this.http.put<IAboutUs>(`${this.resourceUrl}/${this.getAboutUsIdentifier(aboutUs)}`, aboutUs, { observe: 'response' });
  }

  partialUpdate(aboutUs: PartialUpdateAboutUs): Observable<EntityResponseType> {
    return this.http.patch<IAboutUs>(`${this.resourceUrl}/${this.getAboutUsIdentifier(aboutUs)}`, aboutUs, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAboutUs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAboutUs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAboutUsIdentifier(aboutUs: Pick<IAboutUs, 'id'>): number {
    return aboutUs.id;
  }

  compareAboutUs(o1: Pick<IAboutUs, 'id'> | null, o2: Pick<IAboutUs, 'id'> | null): boolean {
    return o1 && o2 ? this.getAboutUsIdentifier(o1) === this.getAboutUsIdentifier(o2) : o1 === o2;
  }

  addAboutUsToCollectionIfMissing<Type extends Pick<IAboutUs, 'id'>>(
    aboutUsCollection: Type[],
    ...aboutusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aboutuses: Type[] = aboutusesToCheck.filter(isPresent);
    if (aboutuses.length > 0) {
      const aboutUsCollectionIdentifiers = aboutUsCollection.map(aboutUsItem => this.getAboutUsIdentifier(aboutUsItem));
      const aboutusesToAdd = aboutuses.filter(aboutUsItem => {
        const aboutUsIdentifier = this.getAboutUsIdentifier(aboutUsItem);
        if (aboutUsCollectionIdentifiers.includes(aboutUsIdentifier)) {
          return false;
        }
        aboutUsCollectionIdentifiers.push(aboutUsIdentifier);
        return true;
      });
      return [...aboutusesToAdd, ...aboutUsCollection];
    }
    return aboutUsCollection;
  }
}
