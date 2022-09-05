import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { Size } from 'app/entities/enumerations/size.model';

export interface IProduct {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  sized?: Size;
  imageContentType?: string | null;
  image?: string | null;
  ehsanComment?: string;
  isActive?: boolean;
  productCategory?: IProductCategory | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public price?: number,
    public sized?: Size,
    public imageContentType?: string | null,
    public image?: string | null,
    public ehsanComment?: string,
    public isActive?: boolean,
    public productCategory?: IProductCategory | null
  ) {
    this.isActive = this.isActive ?? false;
  }
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
