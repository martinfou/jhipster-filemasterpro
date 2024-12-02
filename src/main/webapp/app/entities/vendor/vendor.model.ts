export interface IVendor {
  id: number;
  name?: string | null;
  contactEmail?: string | null;
  contactPhone?: string | null;
  address?: string | null;
}

export type NewVendor = Omit<IVendor, 'id'> & { id: null };
