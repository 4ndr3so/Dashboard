import Loading from "../components/ui/Loading";
import ErrorBox from "../components/ui/Error";
import PetsBar from "../components/charts/PetsBar";
import { useAnimalCounts } from "../hooks/useAnimalCounts";

export default function Dashboard() {
  const year = 2025; // make this a select/dropdown if you like
  const { data, loading, error } = useAnimalCounts(year);

  return (
    <div style={{ padding: 16, display: "grid", gap: 16 }}>
      <h1 style={{ margin: 0 }}>Toronto Licenses Dashboard</h1>
      {loading && <Loading />}
      {error && <ErrorBox message={error} />}
      {data && <PetsBar data={{ Dog: 1234, Cat: 567 }} title={`Favorite pet type (${year})`} />}
    </div>
  );
}
