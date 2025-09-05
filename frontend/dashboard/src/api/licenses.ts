import { AnimalCounts } from "../types/licenses";
import { http } from "./client";


export function getAnimalCountsByYear(year: number) {
  return http<AnimalCounts>(`/api/licenses/${year}/by-animal`);
}