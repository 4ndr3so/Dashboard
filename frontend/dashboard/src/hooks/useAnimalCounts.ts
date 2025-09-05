import { useEffect, useState } from "react";
import { getAnimalCountsByYear } from "../api/licenses";
import type { AnimalCounts } from "../types/licenses";

export function useAnimalCounts(year: number) {
  const [data, setData] = useState<AnimalCounts | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setErr] = useState<string | null>(null);

  useEffect(() => {
    let alive = true;
    setLoading(true);
    setErr(null);

    getAnimalCountsByYear(year)
      .then((d) => { if (alive) setData(d); })
      .catch((e) => { if (alive) setErr(e instanceof Error ? e.message : String(e)); })
      .finally(() => { if (alive) setLoading(false); });

    return () => { alive = false; };
  }, [year]);

  return { data, loading, error };
}
